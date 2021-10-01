import { HttpEvent, HttpEventType } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NotificationService } from 'src/app/core/notification/service/notification.service';
import { NotificationType } from 'src/app/enums/notification-type.enum';
import { FileUploadStatus } from 'src/app/model/file-upload.status';
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
    //file: File;
    photo: string;
    fileStatus = new FileUploadStatus();

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
        this.fileStatus.percentage = 50;
    }

    upload() {
        const description = this.photoForm.get('description').value;
        const allowComments = this.photoForm.get('allowComments').value;
        this.subs.add(
            this.photoService.upload(description, allowComments, this.photo)
            .subscribe(
                (event: HttpEvent<any>) => {
                    this.reportUploadProgress(event);
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
        const reader = new FileReader();   
        reader.onload = ev => this.photo = ev.target.result as string;
        reader.readAsDataURL(file);
    }

    private reportUploadProgress(event: HttpEvent<any>): void {
        switch(event.type) {
            case HttpEventType.UploadProgress:
                this.fileStatus.percentage = Math.round(100 * event.loaded / event.total);
                this.fileStatus.status = 'progress';
                break;
            case HttpEventType.Response:
                if(event.status === 200) {
                    this.notificationService
                        .sendNotification(NotificationType.SUCCESS, 'Photo Uploaded');
                    this.router.navigate(['']);
                    this.fileStatus.percentage = 100;
                    this.fileStatus.status = 'done';
                } else {
                    this.notificationService
                        .sendNotificationError('Error on photo upload');
                    this.fileStatus.percentage = 100;
                    this.fileStatus.status = 'done';
                    break;
                }
            default:
                break;
        }
    }

    ngOnDestroy(): void {
        this.subs.unsubscribe();
    }

}
