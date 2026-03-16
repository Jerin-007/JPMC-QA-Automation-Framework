package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {
    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            System.out.println("ExtentManager: Initializing HTML Report Engine...");
            // 1. Tell it where to save the physical HTml FILE
            String reportPath = System.getProperty("user.dir") + "/target/Executive_Report.html";
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);

            // 2. Configure the Executive UI
            sparkReporter.config().setTheme(Theme.DARK);
            sparkReporter.config().setDocumentTitle("J.P. Morgan QA Automation Run");
            sparkReporter.config().setReportName("UI Navigation & Hover Execution Results");

            // 3. Attach the UI to the Engine
            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);

            // 4. Add Environment Meta-Data
            extent.setSystemInfo("QA Engineer", "SDET Candidate");
            extent.setSystemInfo("Application", "J.P. Morgan Global Homepage");
            extent.setSystemInfo("Operating System", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));


        }
        return extent;

    }
}
