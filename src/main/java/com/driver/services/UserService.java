package com.driver.services;


import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.model.WebSeries;
import com.driver.repository.UserRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebSeriesRepository webSeriesRepository;

    // -----------------------------------------------------------------------------------------
                                                                                    // 1st API - done
    public Integer addUser(User user){

        // Just simply add the user to the Db and return the userId returned by the repository
        User user1 = userRepository.save(user);
        return user1.getId();
    }

                                                                                    // 2nd API - done
    public Integer getAvailableCountOfWebSeriesViewable(Integer userId){

        // Return the count of all webSeries that a user can watch based on his ageLimit and subscriptionType
        // Hint: Take out all the Web-series from the WebRepository

        Integer count = 0;

        User user = userRepository.findById(userId).get();

        List<WebSeries> webSeriesList = webSeriesRepository.findAll();
        SubscriptionType userSubscriptionType = user.getSubscription().getSubscriptionType();

        for(WebSeries series : webSeriesList)
        {
            if(userSubscriptionType == SubscriptionType.BASIC && series.getSubscriptionType() == SubscriptionType.BASIC
                                                              && series.getAgeLimit() <= user.getAge()){
                count++;
            }
            else if(userSubscriptionType == SubscriptionType.PRO && series.getSubscriptionType() == SubscriptionType.PRO
                                && series.getAgeLimit() <= user.getAge()) {
                count++;
            }
            else if(userSubscriptionType == SubscriptionType.ELITE && series.getSubscriptionType() == SubscriptionType.ELITE
                    && series.getAgeLimit() <= user.getAge()){
                count++;
            }

        }

        return count;
    }


}
