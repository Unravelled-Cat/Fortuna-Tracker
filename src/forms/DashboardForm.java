package forms;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import raven.chart.bar.HorizontalBarChart;
import raven.chart.data.category.DefaultCategoryDataset;
import raven.chart.data.pie.DefaultPieDataset;
import raven.chart.line.LineChart;
import raven.chart.pie.PieChart;
import components.SimpleForm;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Locale;
import java.util.TreeMap;
import java.util.TreeSet;


/**
 *
 * @author Group 
 * Credits to Ra Ven on youtube for the UI
 */
public class DashboardForm extends SimpleForm {

    public DashboardForm() {
        init();
    }

    @Override
    public void formRefresh() {
        lineChart.startAnimation();
        pieChart1.startAnimation();
        pieChart2.startAnimation();
//        pieChart3.startAnimation();
        barChart1.startAnimation();
        barChart2.startAnimation();
    }

    @Override
    public void formInitAndOpen() {
        System.out.println("init and open");
    }

    @Override
    public void formOpen() {
        System.out.println("Open");
    }

    private void init() {
        setLayout(new MigLayout("wrap,fill,gap 10", "fill"));
        createPieChart();
        createLineChart();
        createBarChart();
    }
    
    // FOR INCOME PIE CHART
    private void createPieChart() {
        pieChart1 = new PieChart();
        JLabel header1 = new JLabel("Income");
        header1.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:+1");
        pieChart1.setHeader(header1);
        pieChart1.getChartColor().addColor(Color.decode("#f87171"), Color.decode("#fb923c"), Color.decode("#fbbf24"), Color.decode("#a3e635"), Color.decode("#34d399"), Color.decode("#22d3ee"), Color.decode("#818cf8"), Color.decode("#c084fc"));
        pieChart1.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:5,5,5,5,$Component.borderColor,,20");
        pieChart1.setDataset(createPieDataIncome());
        add(pieChart1, "split 2,height 290");

        pieChart2 = new PieChart();
        JLabel header2 = new JLabel("Expenses");
        header2.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:+1");
        pieChart2.setHeader(header2);
        pieChart2.getChartColor().addColor(Color.decode("#f87171"), Color.decode("#fb923c"), Color.decode("#fbbf24"), Color.decode("#a3e635"), Color.decode("#34d399"), Color.decode("#22d3ee"), Color.decode("#818cf8"), Color.decode("#c084fc"));
        pieChart2.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:5,5,5,5,$Component.borderColor,,20");
        pieChart2.setDataset(createPieDataExpenses());
        add(pieChart2, "height 290");
    }

    private void createLineChart() {
        lineChart = new LineChart();
        lineChart.setChartType(LineChart.ChartType.CURVE);
        lineChart.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:5,5,5,5,$Component.borderColor,,20");
        add(lineChart);
        createLineChartData();
    }
    
    private void createBarChart() {
    BarChartData chartData = new BarChartData();

    // BarChart 1 for Monthly Income
    barChart1 = new HorizontalBarChart();
    JLabel header1 = new JLabel("Monthly Income");
    header1.putClientProperty(FlatClientProperties.STYLE, ""
            + "font:+1;"
            + "border:0,0,5,0");
    barChart1.setHeader(header1);
    barChart1.setBarColor(Color.decode("#10b981"));
    barChart1.setDataset(chartData.createBarChartData(true)); // Pass true for income
    JPanel panel1 = new JPanel(new BorderLayout());
    panel1.putClientProperty(FlatClientProperties.STYLE, ""
            + "border:5,5,5,5,$Component.borderColor,,20");
    panel1.add(barChart1);
    add(panel1, "split 2,gap 0 20");

    // BarChart 2 for Monthly Expense
    barChart2 = new HorizontalBarChart();
    JLabel header2 = new JLabel("Monthly Expense");
    header2.putClientProperty(FlatClientProperties.STYLE, ""
            + "font:+1;"
            + "border:0,0,5,0");
    barChart2.setHeader(header2);
    barChart2.setBarColor(Color.decode("#f97316"));
    barChart2.setDataset(chartData.createBarChartData(false)); // Pass false for expense
    JPanel panel2 = new JPanel(new BorderLayout());
    panel2.putClientProperty(FlatClientProperties.STYLE, ""
            + "border:5,5,5,5,$Component.borderColor,,20");
    panel2.add(barChart2);
    add(panel2);
    }

    public class BarChartData {

    public DefaultPieDataset<String> createBarChartData(boolean isIncome) {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        try {
            // Connect to the database (assuming SQLite for this example)
            Connection conn = DriverManager.getConnection("jdbc:sqlite:expense.db");
            String tableName = isIncome ? "incomes" : "expenses";
            String sql = "SELECT month, SUM(amount) AS total_amount FROM " + tableName + " GROUP BY month";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            TreeMap<String, Double> monthAmountMap = new TreeMap<>();

            // Iterate through the result set and add data to the dataset
            while (rs.next()) {
                String month = rs.getString("month").trim(); // Trim extra spaces
                double amount = rs.getDouble("total_amount");
                // Add the accumulated income or expense amount for each month to TreeMap
                monthAmountMap.put(month, amount);
            }

            // Close resources
            rs.close();
            pstmt.close();
            conn.close();

            // Sort TreeMap by converting month strings to YearMonth objects
            TreeMap<YearMonth, Double> sortedMonthAmountMap = new TreeMap<>();
            for (String month : monthAmountMap.keySet()) {
                String[] parts = month.split(" ");
                String monthName = parts[0];
                int year = Year.now().getValue(); // Default to current year
                if (parts.length > 1) {
                    year = Integer.parseInt(parts[1]);
                }
                YearMonth yearMonth = YearMonth.of(year, Month.valueOf(monthName.toUpperCase()));
                sortedMonthAmountMap.put(yearMonth, monthAmountMap.get(month));
            }

            // Convert sorted TreeMap back to DefaultPieDataset
            for (YearMonth yearMonth : sortedMonthAmountMap.keySet()) {
                dataset.setValue(yearMonth.getMonth().toString(), sortedMonthAmountMap.get(yearMonth));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle exceptions
        }

        return dataset;
    }
}


   private DefaultPieDataset<String> createPieDataExpenses() { 
    DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

    try {
        // Connect to the database (assuming SQLite for this example)
        Connection conn = DriverManager.getConnection("jdbc:sqlite:expense.db");
        String sql = "SELECT category, SUM(amount) AS total_amount FROM expenses GROUP BY category";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        // Iterate through the result set and add data to the dataset
        while (rs.next()) {
            String description = rs.getString("category");
            double amount = rs.getDouble("total_amount");
            dataset.setValue(description, amount);
        }

        // Close resources
        rs.close();
        pstmt.close();
        conn.close();
    } catch (SQLException ex) {
        ex.printStackTrace();
        // Handle exceptions
    }

    return dataset;
}

private DefaultPieDataset<String> createPieDataIncome() {
    DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

    try {
        // Connect to the database (assuming SQLite for this example)
        Connection conn = DriverManager.getConnection("jdbc:sqlite:expense.db");
        String sql = "SELECT category, SUM(amount) AS total_amount FROM incomes GROUP BY category";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        // Iterate through the result set and add data to the dataset
        while (rs.next()) {
            String description = rs.getString("category");
            double amount = rs.getDouble("total_amount");
            dataset.setValue(description, amount);
        }

        // Close resources
        rs.close();
        pstmt.close();
        conn.close();
    } catch (SQLException ex) {
        ex.printStackTrace();
        // Handle exceptions
    }

    return dataset;
}
    
    
    private void createLineChartData() {
       DefaultCategoryDataset<String, String> categoryDataset = new DefaultCategoryDataset<>();

    try {
        // Connect to the expense database (assuming SQLite for this example)
        Connection expenseConn = DriverManager.getConnection("jdbc:sqlite:expense.db");
        String expenseSql = "SELECT date, amount FROM expenses";
        PreparedStatement expensePstmt = expenseConn.prepareStatement(expenseSql);
        ResultSet expenseRs = expensePstmt.executeQuery();

        // Map to store total expense amount for each date
        TreeMap<String, Double> expenseDateAmountMap = new TreeMap<>();

        // Iterate through the expense result set and accumulate amounts per date
        while (expenseRs.next()) {
            String date = expenseRs.getString("date");
            double amount = expenseRs.getDouble("amount");

            // Update the total amount for the current date
            expenseDateAmountMap.merge(date, amount, Double::sum);
        }

        // Close expense resources
        expenseRs.close();
        expensePstmt.close();
        expenseConn.close();

        // Connect to the income database (assuming SQLite for this example)
        Connection incomeConn = DriverManager.getConnection("jdbc:sqlite:expense.db");
        String incomeSql = "SELECT date, amount FROM incomes";
        PreparedStatement incomePstmt = incomeConn.prepareStatement(incomeSql);
        ResultSet incomeRs = incomePstmt.executeQuery();

        // Map to store total income amount for each date
        TreeMap<String, Double> incomeDateAmountMap = new TreeMap<>();

        // Iterate through the income result set and accumulate amounts per date
        while (incomeRs.next()) {
            String date = incomeRs.getString("date");
            double amount = incomeRs.getDouble("amount");

            // Update the total amount for the current date
            incomeDateAmountMap.merge(date, amount, Double::sum);
        }

        // Close income resources
        incomeRs.close();
        incomePstmt.close();
        incomeConn.close();

        // Custom comparator for sorting dates chronologically
       Comparator<String> dateComparator = (date1, date2) -> {
           DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM/dd/yyyy", Locale.ENGLISH);
           LocalDate localDate1 = LocalDate.parse(date1.trim(), formatter);
           LocalDate localDate2 = LocalDate.parse(date2.trim(), formatter);
           return localDate1.compareTo(localDate2);
       };

        // Merge the expense and income maps, keeping track of all dates
        TreeSet<String> allDates = new TreeSet<>(dateComparator);
        allDates.addAll(expenseDateAmountMap.keySet());
        allDates.addAll(incomeDateAmountMap.keySet());

        // Add accumulated income amounts to the dataset
        for (String date : allDates) {
            double incomeAmount = incomeDateAmountMap.getOrDefault(date, 0.0);
            double expenseAmount = expenseDateAmountMap.getOrDefault(date, 0.0);
            categoryDataset.addValue(incomeAmount, "Income", date);
            categoryDataset.addValue(expenseAmount, "Expense", date);
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        // Handle exceptions
    }

    // Set the dataset to the line chart
    lineChart.setCategoryDataset(categoryDataset);
    lineChart.getChartColor().addColor(Color.decode("#38bdf8"), Color.decode("#fb7185"), Color.decode("#34d399"));
    JLabel header = new JLabel("Income and Expense Data");
    header.putClientProperty(FlatClientProperties.STYLE, ""
            + "font:+1;"
            + "border:0,0,5,0");
    lineChart.setHeader(header);
}

    private LineChart lineChart;
    private HorizontalBarChart barChart1;
    private HorizontalBarChart barChart2;
    private PieChart pieChart1;
    private PieChart pieChart2;
//    private PieChart pieChart3;
}




