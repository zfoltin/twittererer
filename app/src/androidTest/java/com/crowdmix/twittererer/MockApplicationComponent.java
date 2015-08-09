package com.crowdmix.twittererer;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {MockApplicationModule.class})
public interface MockApplicationComponent extends App.BaseApplicationComponent {
}
