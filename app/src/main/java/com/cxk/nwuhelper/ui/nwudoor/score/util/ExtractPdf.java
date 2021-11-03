package com.cxk.nwuhelper.ui.nwudoor.score.util;

import android.util.Log;

import com.cxk.nwuhelper.MyApplication;


import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.util.List;

import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.RectangularTextContainer;
import technology.tabula.Table;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

public class ExtractPdf {
    public void extractPdf() throws IOException {
        Log.d("pdftest", "extractPdf: "+MyApplication.context.getFilesDir());
        PDDocument pd = PDDocument.load(new File(MyApplication.context.getFilesDir(), "temp.pdf"));


        int totalPages = pd.getNumberOfPages();
        System.out.println("Total Pages in Document: "+totalPages);

        ObjectExtractor oe = new ObjectExtractor(pd);
        SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();
        Page page = oe.extract(1);

        // extract text from the table after detecting
//        List<Table> table = sea.extract(page);
//        for(Table tables: table) {
//            List<List<RectangularTextContainer>> rows = tables.getRows();
//
//            for(int i=0; i<rows.size(); i++) {
//                List<RectangularTextContainer> cells = rows.get(i);
//
//                for(int j=0; j<cells.size(); j++) {
//                    System.out.print(cells.get(j).getText()+"|");
//                }
//
//                // System.out.println();
//            }
//        }

    }

}
