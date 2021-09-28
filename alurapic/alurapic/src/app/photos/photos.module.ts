import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';

import { PhotoListComponent } from './photo-list/photo-list.component';
import { PhotoComponent } from './photo/photo.component';
import { PhotoFormComponent } from './photo-form/photo-form.component';
import { PhotosComponent } from './photo-list/photos/photos.component';
import { FormsModule } from '@angular/forms';
import { FiterByDescriptionPipe } from './photo-list/fiter-by-description.pipe';



@NgModule({
  declarations: [PhotoComponent, PhotoListComponent, PhotoFormComponent, PhotosComponent, FiterByDescriptionPipe],
  imports: [
    CommonModule,
    HttpClientModule,
    FormsModule
  ],
  exports: [ PhotoComponent ]
})
export class PhotosModule { }
