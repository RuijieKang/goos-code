package auctionsniper.humble.bootstrap;

public class Main {
    private static final int ARG_HOSTNAME = 0;
    private static final int ARG_USERNAME = 1;
    private static final int ARG_PASSWORD = 2;

    public static void main(String... args) throws Exception {
        Application application = new ApplicationFactory(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]).createApplication();
        application.start();
    }
}
