package enums;

public enum SupportedBrowsers {
    Chrome(".\\src\\test\\resources\\chromedriver_win32\\chromedriver.exe"),
    Firefox(".\\src\\test\\resources\\geckodriver-v0.32.0-win32\\geckodriver.exe"),
    Edge("src\\test\\resources\\edgedriver_win64\\msedgedriver.exe"),
    IE(".\\src\\test\\resources\\IEDriverServer_Win32_4.6.0\\IEDriverServer.exe");

    public final String path;

    SupportedBrowsers(String webDriverPath) {
        this.path = webDriverPath;
    }
}
