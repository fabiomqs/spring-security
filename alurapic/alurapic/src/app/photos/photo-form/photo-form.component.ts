import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NotificationService } from 'src/app/core/notification/service/notification.service';
import { NotificationType } from 'src/app/enums/notification-type.enum';
import { SubSink } from 'subsink';
import { Photo } from '../photo/photo';
import { PhotoService } from '../photo/service/photo.service';

@Component({
    selector: 'app-photo-form',
    templateUrl: './photo-form.component.html'
})
export class PhotoFormComponent implements OnInit, OnDestroy {

    private subs = new SubSink();

    photoForm: FormGroup;
    file: File;

    constructor(
        private formBuilder: FormBuilder,
        private photoService: PhotoService,
        private router:Router,
        private notificationService: NotificationService
    ) { }
    

    ngOnInit(): void {
        this.photoForm = this.formBuilder.group({
            file: ['',Validators.required],
            description: ['', Validators.maxLength(300)],
            allowComments: [true]
        })
    }

    upload() {
        const description = this.photoForm.get('description').value;
        const allowComments = this.photoForm.get('allowComments').value;
        this.subs.add(
            this.photoService.upload(description, allowComments, this.file)
            .subscribe(
                (photo: Photo) => {
                    this.notificationService
                        .sendNotification(NotificationType.SUCCESS, 'Photo Uploaded');
                    this.router.navigate(['']);
                },
                err=> {
                    this.notificationService.sendNotificationError(err.error.message);
                    console.error(err)
                }
            )
        )
    }

    onPhotoChange(event: Event):void {
        const target = event.target as HTMLInputElement;
        const file: File = (target.files as FileList)[0];
        this.file = file;       
    }

    ngOnDestroy(): void {
        this.subs.unsubscribe();
    }

}
