import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { switchMap, tap } from 'rxjs/operators';

import { SubSink } from 'subsink';

import { NotificationService } from 'src/app/core/notification/service/notification.service';
import { NotificationType } from 'src/app/enums/notification-type.enum';
import { PhotoService } from '../../service/photo.service';

@Component({
    selector: 'app-photo-comment',
    templateUrl: './photo-comment.component.html',
    styleUrls: ['photo-comment.component.css']
})
export class PhotoCommentComponent implements OnInit, OnDestroy {

    private subs = new SubSink();

    @Input() photoId: number
    commentForm: FormGroup;

    comments$: Observable<Comment[]>;

    constructor(
        private photoService:PhotoService,
        private formBuilder: FormBuilder,
        private notificationService: NotificationService
    ) { }
    

    ngOnInit(): void {
        this.comments$ = this.photoService.getComments(this.photoId);
        this.commentForm = this.formBuilder.group({
            comment: ['', 
                [
                    Validators.required,
                    Validators.maxLength(255)
                ]
            ]
        });
    }

    submitComment() {
        const comment = this.commentForm.get('comment').value as string;
        
            this.comments$ = this.photoService
                .addComment(this.photoId, comment)
                .pipe(switchMap(
                    () => this.photoService.getComments(this.photoId)))
                .pipe(tap(() => {
                    this.commentForm.reset();
                    this.notificationService.sendNotification(
                        NotificationType.SUCCESS, 'Comment Added Successfully');
                }))
                
        
    }

    //.subscribe(
    //    () => {
    //        this.commentForm.reset();
    //        this.notificationService.sendNotification(
    //            NotificationType.SUCCESS, 'Comment Added Successfully')
    //    },
    //    err => this.notificationService.sendNotificationError(err.error.message)
    //)

    ngOnDestroy(): void {
        this.subs.unsubscribe();
    }

}
