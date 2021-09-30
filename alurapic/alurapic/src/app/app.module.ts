import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NotificationModule } from './core/notification/notification.module';

import { ErrorsModule } from './errors/errors.module';
import { HomeModule } from './home/home.module';
import { PhotosModule } from './photos/photos.module';
import { APIURL, PAGESIZE } from './core/tokens';

import {environment} from '../environments/environment';
import { CoreModule } from './core/core.module';

@NgModule({
    declarations: [
        AppComponent
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        PhotosModule,
        ErrorsModule,
        HomeModule,
        NotificationModule,
        CoreModule
    ],
    providers: [
        {provide: APIURL, useValue: environment.apiUrl},
        {provide: PAGESIZE, useValue: environment.pageSize}   
    ],
    bootstrap: [AppComponent]
})
export class AppModule { }
