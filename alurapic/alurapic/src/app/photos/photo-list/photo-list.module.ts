import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { PhotoListComponent } from './photo-list.component';
import { PhotosComponent } from './photos/photos.component';
import { LoadButtonComponent } from './load-button/load-button.component';
import { FiterByDescriptionPipe } from './fiter-by-description.pipe';
import { PhotoModule } from '../photo/photo.module';



@NgModule({
  declarations: [
      PhotoListComponent,
      PhotosComponent,
      LoadButtonComponent,
      FiterByDescriptionPipe
  ],
  imports: [
    CommonModule,
    FormsModule,
    PhotoModule
  ]
})
export class PhotoListModule { }
