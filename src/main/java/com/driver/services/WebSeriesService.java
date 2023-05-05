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

       if(webSeriesRepository.findBySeriesName(webSeriesEntryDto.getSeriesName()) != null) {
           throw new Exception("Series is already present");
       }

        ProductionHouse productionHouse = productionHouseRepository.findById(webSeriesEntryDto.getProductionHouseId()).get();

        WebSeries webSeries = new WebSeries();
        webSeries.setSeriesName(webSeriesEntryDto.getSeriesName());
        webSeries.setAgeLimit(webSeriesEntryDto.getAgeLimit());
        webSeries.setRating(webSeriesEntryDto.getRating());
        webSeries.setSubscriptionType(webSeriesEntryDto.getSubscriptionType());
        webSeries.setProductionHouse(productionHouse);

        productionHouse.getWebSeriesList().add(webSeries);
        List<WebSeries> webSeriesList = productionHouse.getWebSeriesList();

        double avgRating = 0;
        double totalWebSeries = productionHouse.getWebSeriesList().size();

        for(WebSeries series : webSeriesList){
            avgRating += series.getRating();
        }

        productionHouse.setRatings( (avgRating/totalWebSeries) * 1.0);

        webSeriesRepository.save(webSeries);
        productionHouseRepository.save(productionHouse);

        return webSeries.getId();
    }

}
