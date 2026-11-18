import config.ConfigManager;

public class configPractice {
    public static void main(String[] args) {
        String url = ConfigManager.getProperty("base.url");
        String qaUrl = ConfigManager.getProperty("qa.base.url");
        String stgUrl = ConfigManager.getProperty("stg.base.url");
        String devUrl = ConfigManager.getProperty("dev.base.url");
        System.out.println(url);
        System.out.println(qaUrl);
        System.out.println(devUrl);
        System.out.println(stgUrl);

        String env= System.getProperty("env","petstore");
        System.out.println("Selected env is "+ env);
    }

}
