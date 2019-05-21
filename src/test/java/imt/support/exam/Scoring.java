package imt.support.exam;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

public class Scoring implements TestWatcher {

    private Report report(ExtensionContext context) {
        return context.getRoot().getStore(ExtensionContext.Namespace.create(getClass()))
                      .getOrComputeIfAbsent(getClass(),
                                            k -> new Report(),
                                            Report.class);
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        final Method testcase = context.getRequiredTestMethod();
        Optional.ofNullable(testcase.getDeclaredAnnotation(Points.class))
                .ifPresent(pts -> report(context).addScore(new NoScore(testcase, pts)));
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        final Method testcase = context.getRequiredTestMethod();
        Optional.ofNullable(testcase.getDeclaredAnnotation(Points.class))
                .ifPresent(pts -> report(context).addScore(new Score(testcase, pts)));
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        final Method testcase = context.getRequiredTestMethod();
        Optional.ofNullable(testcase.getDeclaredAnnotation(Points.class))
                .ifPresent(pts -> report(context).addScore(new AbortedScore(testcase, pts)));
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        final Method testcase = context.getRequiredTestMethod();
        Optional.ofNullable(testcase.getDeclaredAnnotation(Points.class))
                .ifPresent(pts -> report(context).addScore(new FailedScore(testcase, pts)));
    }

    @Target(METHOD)
    @Retention(RUNTIME)
    public @interface Points {
        int value();
    }

    static class Report implements ExtensionContext.Store.CloseableResource {
        private final List<Score> scores;

        Report() { this.scores = new ArrayList<>(); }

        void addScore(Score s) { this.scores.add(s); }

        @Override
        public void close() {
            System.err.println("===  EXERCISE PROGRESS");
            this.scores.forEach(System.err::println);
            System.err.printf("===  EXERCISE SCORE: %d pts (out of %d)\n",
                              obtained(),
                              maximum());
        }

        private int obtained() { return this.scores.stream().mapToInt(Score::points).sum(); }

        private int maximum() { return this.scores.stream().mapToInt(Score::maxPoints).sum(); }
    }

    static class Score {
        final Method testcase;
        final Points points;

        Score(Method testcase, Points points) {
            this.testcase = testcase;
            this.points = points;
        }

        String name() { return this.testcase.getName(); }

        Optional<String> displayName() {
            return Optional.ofNullable(this.testcase.getDeclaredAnnotation(DisplayName.class))
                           .map(DisplayName::value);
        }

        public int points() { return maxPoints(); }

        int maxPoints() { return this.points.value(); }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder().append(valueString());
            displayName().ifPresent(n -> sb.append("  ").append(n));
            sb.append("  (").append(name()).append(')');
            return sb.toString();
        }

        protected String valueString() { return String.format(" +%d", points()); }
    }

    static class NoScore extends Score {
        NoScore(Method testcase, Points points) { super(testcase, points); }

        public int points() { return 0; }

        protected String valueString() { return "  ·"; }
    }

    static class FailedScore extends NoScore {
        FailedScore(Method testcase, Points points) { super(testcase, points); }

        protected String valueString() { return ">>>"; }
    }

    static class AbortedScore extends NoScore {
        AbortedScore(Method testcase, Points points) { super(testcase, points); }

        protected String valueString() { return "  -"; }
    }
}
