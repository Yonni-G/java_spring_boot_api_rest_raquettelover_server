package com.yonni.raquettelover.service;

import com.yonni.raquettelover.entity.User;

public interface UserSubscriptionPlaceService {
    void createSubscription(User user, Long placeId);
}
