package com.zedeff.twittererer;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {MockApplicationModule.class})
public interface MockApplicationComponent extends App.BaseApplicationComponent {
}
