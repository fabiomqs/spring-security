import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PhotosDetailsComponent } from './photos-details.component';
import { PhotoModule } from '../photo/photo.module';



@NgModule({
  declarations: [
    PhotosDetailsComponent
  ],
  imports: [
    CommonModule,
    PhotoModule
  ], 
  exports: [
      PhotosDetailsComponent
  ]
})
export class PhotosDetailsModule { }
