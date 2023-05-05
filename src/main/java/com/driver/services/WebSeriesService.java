package com.driver.services;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebSeriesService {

    @Autowired
    WebSeriesRepository webSeriesRepository;

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    // -----------------------------------------------------------------------------------------
                                                                                        // 1st API - done

    public Integer addWebSeries(WebSeriesEntryDto webSeriesEntryDto)throws  Exception{

        // Add a webSeries to the database and update the ratings of the productionHouse
        // In case the seriesName is already present in the Db throw Exception("Series is already present")
        // Use function written in Repository Layer for the same
        // Don't forget to save the production and web-series Repo


       WebSeries webSeries = webSeriesRepository.findBySeriesName(webSeriesEntryDto.getSeriesName());

       if(webSeries != null)
           throw new Exception("Series is already present");

       WebSeries webSeries1 = new WebSeries();
        webSeries1.setSeriesName(webSeriesEntryDto.getSeriesName());
        webSeries1.setAgeLimit(webSeriesEntryDto.getAgeLimit());
        webSeries1.setRating(webSeriesEntryDto.getRating());
        webSeries1.setSubscriptionType(webSeriesEntryDto.getSubscriptionType());

        ProductionHouse productionHouse = productionHouseRepository.findById(webSeriesEntryDto.getProductionHouseId()).get();
        List<WebSeries> webSeriesList = productionHouse.getWebSeriesList();

        webSeriesList.add(webSeries);

        int size = webSeriesList.size();
        double avgRating = 0;

        for(WebSeries series : webSeriesList){
            avgRating += series.getRating();
        }

        productionHouse.setRatings(avgRating/(size * 1.0));
        productionHouse.setWebSeriesList(webSeriesList);
        webSeries1.setProductionHouse(productionHouse);

        webSeriesRepository.save(webSeries1);
        productionHouseRepository.save(productionHouse);

        return webSeries1.getId();
    }

}
