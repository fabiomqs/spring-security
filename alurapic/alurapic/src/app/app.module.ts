import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NotificationModule } from './core/notification/notification.module';
import { ErrorsModule } from './errors/errors.module';
import { PhotosModule } from './photos/photos.module';
import { APIURL, LOGURL, PAGECOMMENTSSIZE, PAGESIZE } from './core/tokens';
import {environment} from '../environments/environment';
import { CoreModule } from './core/core.module';

@NgModule({
    declarations: [
        AppComponent
    ],
    imports: [
        BrowserModule,
        PhotosModule,
        ErrorsModule,
        CoreModule,
        NotificationModule,
        AppRoutingModule
    ],
    providers: [
        {provide: APIURL, useValue: environment.apiUrl},
        {provide: LOGURL, useValue: environment.logUrl},
        {provide: PAGESIZE, useValue: environment.pagePhotosSize}, 
        {provide: PAGECOMMENTSSIZE, useValue: environment.pageCommentsSize}
    ],
    bootstrap: [AppComponent]
})
export class AppModule { }
