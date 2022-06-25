package com.example.SaveMon.enums;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum DirectionPayment {
    SPORT("https://cdn4.iconfinder.com/data/icons/supermarket-34/512/sporting-goods-ball-sport-512.png"),
    TRIPS("https://icon-library.com/images/travel-icon/travel-icon-16.jpg "),
    HEALTH("https://image.flaticon.com/icons/png/512/121/121731.png"),
    ENTERTAINMENT("https://icon-library.com/images/entertainment-icon/entertainment-icon-13.jpg"),
    FOOD("https://image.flaticon.com/icons/png/512/240/240786.png"),
    HOUSE("https://icons-for-free.com/iconfiles/png/512/home+house+icon-1320087051499461146.png"),
    GAMES("https://icons-for-free.com/iconfiles/png/512/fun+games+joystick+icon-1320184693372218944.png"),
    VEHICLE("https://icons.iconarchive.com/icons/icons8/ios7/512/Transport-Bus-2-icon.png"),
    ALCOHOL("https://cdn2.iconfinder.com/data/icons/wine-beer-and-champagne-bottles-and-bocals/154/wine-bottle-512.png"),
    STUDY("https://www.shareicon.net/data/512x512/2015/08/05/80436_study_512x512.png"),
    COMMUNAL("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAOEAAADhCAMAAAAJbSJIAAAAeFBMVEX///8aGhoAAACLi4vPz8+CgoKMjIyIiIgXFxcTExMPDw8WFhbv7+/7+/sJCQkSEhKamprg4OAvLy95eXm6urolJSXBwcEeHh6pqak1NTVjY2P19fU6OjqioqJTU1NMTEzq6upycnLLy8tHR0ezs7NoaGhcXFxSUlIJfuZAAAAEdklEQVR4nO2dCXLbMAxFSXqT5CV72jRt46ZNcv8bVrITb+LiGYsD6Q3eCfAGkD4N04kxiqIoiqIoiqIoiqIoSrcspAvIzfVSuoLMTB6lK8jM7Ld0BZmZuRvpEvIydTPpEvIydd+epWvIysi579I1ZGXkylvpGrIydda9SheRk5GzqzfpInJSCxbopKhH1KKTohEsrsBJUY9o3UJwUmwEyyfpMvLRjGjdQu5nik0H7eqHdB3Z2AqCk2I7otZNpAvJxadg4ahJsR1RcFJ8CZY/pSvJxOeIcpNiJ7h6ly4lD18jik2KnSA1KXYjSk2KfQet+yVdTA4OBK+QSbEfUWhSHAoik+JgROsWApPiWPBOupzuORxRZFIcCRKT4mhE66TAfaN9LAhMiuMRtRXuG+2TDvKSoiVIS4qTEa2TAnY16FQQlxSnI4pLipagdffSNXVKa0Rt9SFdU6e0OwhLCp8gKik8gkW1lq6qQ9rPYN3Ca+mqOsTTQVs+gZLCJ4hKCt+I2upFuqzu8AqSksI7oqSk8AsW5Vq6sK7wjygoKfwdtOUtJSkCgpykCIyorf5KV9YRIUHrxtKldUNoRDFJERQs5mvp2johOKKUpAgLzh8QSREcUUpSRAQZSREeUUhSRDrISIqYIOIzRWxEEUkRFZzPpcu7nNiIIpIiLgjYPkVHlJAU8Q5a90e6wEtJCBartXSFF5IQtG4qXeGFJJ5BW1TSFV7ILCFoV6Nxgtde3+BLjWij6BL0+siaGtEz6PdvnPGCZ4xoUrDXL9ouBHvdQR3RMwRH0hIx8IL4EcULakwMvYP4Ee1CsNdvUXxM6DM4dMFJ7JPsqjhLsNcjOr6bRJjdnqHYb8EEN2XasN8jmuJ9BRe8T7+E+n1US/IwZ3fQXCdbOHDBtUu9Zgb9Fq35k2rh0AXHScFhj6gxHxVc8B/9GzaTOJIOvoPJ7/EHL3gTT4rhC5q36IF0+M+geY3fFhp+B83PK7jg9+iVvYGfZBqeY0lBEDST2L1ZwIhGkwIhGFtdAGKi5jHy8wNEB81tCRf8Ff4NEOEtaswimBQQQXMX/CEeY0TNOJQUFMHg6oIyosElN0bQfPMvuTEjGlpycwTX/qRgHNU2+JfcnA4GltwgQfPXlxSct2hgyU0SXFpPUpBG1P9XykiCvtUFKCaMd8mN6qBvyc0S9KwuYILt1QXrGTTPraSAdbC95KYJtpKCdJLZcLrkxgmeLrlpI9q6n8cTPFldwGKiZl0U7A6eLLmBgserC9xbtOalggseLbmBI2qWh0lBFDz+n3DAETXrqmALHi65kSN6mBRMQfO0u5/HO6pt2N/Pg3bweZcUUMH9H0tivkUPVhdUQfNjxR7R3ZIbK2geSnJMmN2Sm9vBxfbiDFfwc8kNFrxx7GfQmN8Vu4PbJTdZcLPkxp5kGpolN1qwWXKjR7RZcrMFxw4dE6a5n8fuoLl3cMHlA1zQTOHPYH0ghXfQvE+kK8jMa6//znYHLAH/OCXOQroARVEURVEURVEURVEUHP8BQAspPU++uA4AAAAASUVORK5CYII=");


    private String image;

    DirectionPayment(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

}
