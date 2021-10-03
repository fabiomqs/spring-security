import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PhotosDetailsComponent } from './photos-details.component';
import { PhotoModule } from '../photo/photo.module';
import { PhotoCommentComponent } from './photo-comment/photo-comment.component';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { VmessageModule } from 'src/app/shared/components/vmessage/vmessage.module';
import { PhotoOwnerOnlyDirective } from './photo-directives/photo-owner-only.directive';
import { CommentOwnerOnlyDirective } from './photo-directives/comment-owner-only.directive';



@NgModule({
  declarations: [
    PhotosDetailsComponent,
    PhotoCommentComponent,
    PhotoOwnerOnlyDirective,
    CommentOwnerOnlyDirective
  ],
  imports: [
    CommonModule,
    PhotoModule,
    RouterModule,
    ReactiveFormsModule,
    VmessageModule
  ], 
  exports: [
      PhotosDetailsComponent,
      PhotoCommentComponent
  ]
})
export class PhotosDetailsModule { }
