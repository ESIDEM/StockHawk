package com.udacity.stockhawk.ui;


import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;


import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StockDetailsActivity extends AppCompatActivity {

    public static final String TAG = StockDetailsActivity.class.getName();



    public static final String STOCK_SYMBOL = "stock_symbol";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_details);

        Intent intentThatStartedActivity = getIntent();
        if (intentThatStartedActivity != null) {
            Log.d(TAG, "Received intent");
            if (intentThatStartedActivity.hasExtra(STOCK_SYMBOL)) {
                String symbol = intentThatStartedActivity.getStringExtra(STOCK_SYMBOL);
                Log.d(TAG, "intent brings symbol " + symbol);

                String[] chartData = getHistoryData(symbol);

                if (chartData != null && chartData.length > 0) {
                    plotLineChart(chartData, symbol);
                }
            }
        }

    }


    private String[] getHistoryData(String symbol) {
        String[] result = null;

        Cursor cursor = getContentResolver().query(
                Contract.Quote.makeUriForStock(symbol),
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int symbolIndex = cursor.getColumnIndex(Contract.Quote.COLUMN_SYMBOL);
            int priceIndex = cursor.getColumnIndex(Contract.Quote.COLUMN_PRICE);
            int absoluteChangeIndex = cursor.getColumnIndex(Contract.Quote.COLUMN_ABSOLUTE_CHANGE);
            int percentageChangeIndex = cursor.getColumnIndex(Contract.Quote.COLUMN_PERCENTAGE_CHANGE);
            int historyIndex = cursor.getColumnIndex(Contract.Quote.COLUMN_HISTORY);


            TextView tvStockSymbol = (TextView)  findViewById(R.id.tv_stock_symbol);
            tvStockSymbol.setText(cursor.getString(symbolIndex));

            float price = cursor.getFloat(priceIndex);
            String priceAsString = price > 0 ? "+$"+Float.toString(price) : Float.toString(price);
            TextView tvCurrentStockPrice = (TextView)  findViewById(R.id.tv_current_stock_price);

            tvCurrentStockPrice.setText(priceAsString);

            TextView tvAbsoluteChange = (TextView)  findViewById(R.id.tv_absolute_change);

            tvAbsoluteChange.setText(Float.toString(cursor.getFloat(absoluteChangeIndex)));

            float percentageChange = cursor.getFloat(percentageChangeIndex);
            String percentageChangeAsString =
                    percentageChange > 0 ? "+"+Float.toString(percentageChange) : Float.toString(percentageChange);
            TextView tvPercentageChange = (TextView)  findViewById(R.id.tv_percentage_change);
            tvPercentageChange.setText("("+percentageChangeAsString+"%)");

            // split history data per line
            result = cursor.getString(historyIndex).split("\n");

            cursor.close();
        }

        return result;
    }

    private void plotLineChart(String[] chartData, String symbol) {

        final Long[] xAxisValues = new Long[chartData.length];

        List<Entry> entries = new ArrayList<>();
        for (int position = 0; position < chartData.length; position++) {
            String point = chartData[position];
            // split the line by separator 0 - timestamp, 1 - value
            String[] values = point.split(",");
            xAxisValues[position] = Long.valueOf(values[0]);
            entries.add(new Entry(position, Float.valueOf(values[1].trim())));
        }

        LineDataSet dataSet = new LineDataSet(entries, symbol);
        dataSet.setColor(R.color.colorPrimary);

        // retrieve the view line chart
        LineChart lineChart = (LineChart) findViewById(R.id.line_chart);
        LineData lineData = new LineData(dataSet);

        // Add dollar sign to yAxis labels
        final NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        lineData.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return "$" + numberFormat.format(value);
            }
        });

        lineChart.setData(lineData);
        lineChart.setAutoScaleMinMaxEnabled(true);
        lineChart.setKeepPositionOnRotation(true);

        Description description = new Description();
        description.setText(getString(R.string.currency_label));
        lineChart.setDescription(description);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(R.color.colorPrimary);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(true);

        final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float position, AxisBase axis) {
                long timestamp = xAxisValues[xAxisValues.length - (int)position - 1];
                return dateFormat.format(new Date(timestamp));
            }
        });

        // style legend
        Legend legend = lineChart.getLegend();
        legend.setEnabled(true);
        legend.setTextColor(R.color.colorPrimary);

        lineChart.invalidate();
    }




}
