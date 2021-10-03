import { NgModule } from '@angular/core';

import { PhotoComponent } from './photo/photo.component';
import { PhotoListModule } from './photo-list/photo-list.module';
import { PhotoFormModule } from './photo-form/photo-form.module';
import { PhotoModule } from './photo/photo.module';
import { PhotosDetailsModule } from './photos-details/photos-details.module';



@NgModule({
    imports: [
        PhotoListModule,
        PhotoFormModule,
        PhotoModule,
        PhotosDetailsModule
    ],
    exports: [ 
        PhotoComponent
    ]
})
export class PhotosModule { }
