import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { Observable } from 'rxjs';

import { SubSink } from 'subsink';

import { NotificationService } from 'src/app/core/notification/service/notification.service';
import { NotificationType } from 'src/app/enums/notification-type.enum';
import { Photo } from 'src/app/model/photo';
import { PhotoService } from '../service/photo.service';

@Component({
    templateUrl: './photos-details.component.html'
})
export class PhotosDetailsComponent implements OnInit, OnDestroy {

    private subs = new SubSink();

    photo$: Observable<Photo>;
    photoId: number;

    constructor(
        private activatedRoute:ActivatedRoute,
        private router:Router,
        private photoService:PhotoService,
        private notificationService:NotificationService
    ) { }
    
    

    ngOnInit(): void {
        this.photoId = this.activatedRoute.snapshot.params.photoId;
        this.photo$ = this.photoService.findById(this.photoId);
    }

    delete() {
        this.subs.add(
            this.photoService
                .deletePhoto(this.photoId)
                .subscribe(() => {
                    this.notificationService.sendNotification(NotificationType.INFO,
                        'Photo Removedo Successfully');
                    this.router.navigate(['']);
                }, 
                err => this.notificationService.sendNotificationError(err.error.message)
            )
        )
    }

    ngOnDestroy(): void {
        this.subs.unsubscribe();
    }
}
