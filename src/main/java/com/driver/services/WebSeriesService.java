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


        if(isWebSeriesExist(webSeriesEntryDto.getSeriesName())) {
            throw new Exception("Series is already present");
        }

        WebSeries webSeries = new WebSeries(webSeriesEntryDto.getSeriesName(),webSeriesEntryDto.getAgeLimit(),
                                                webSeriesEntryDto.getRating(),webSeriesEntryDto.getSubscriptionType());

        ProductionHouse productionHouse = productionHouseRepository.findById(webSeriesEntryDto.getProductionHouseId()).get();
        List<WebSeries> webSeriesList = productionHouse.getWebSeriesList();

        webSeriesList.add(webSeries);

        int size = webSeriesList.size();
        int avgRating = 0;

        for(WebSeries series : webSeriesList){
            avgRating += series.getRating();
        }

        productionHouse.setRatings(avgRating/size);
        productionHouse.setWebSeriesList(webSeriesList);
        webSeries.setProductionHouse(productionHouse);

        webSeriesRepository.save(webSeries);
        productionHouseRepository.save(productionHouse);


        return webSeries.getId();
    }

        // Function used in 1st API
    public boolean isWebSeriesExist(String name){

        if(webSeriesRepository.findBySeriesName(name).equals(name))
            return true;

        return false;
    }

}
