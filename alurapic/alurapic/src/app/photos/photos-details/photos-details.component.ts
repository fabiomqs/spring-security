import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { Observable } from 'rxjs';

import { SubSink } from 'subsink';

//import { NotificationService } from 'src/app/core/notification/service/notification.service';
import { Photo } from 'src/app/model/photo';
import { PhotoService } from '../service/photo.service';
import { AlertService } from 'src/app/shared/components/alert/service/alert.service';
import { UserService } from 'src/app/core/user/user.service';

@Component({
    templateUrl: './photos-details.component.html'
})
export class PhotosDetailsComponent implements OnInit, OnDestroy {

    private subs = new SubSink();

    photo$: Observable<Photo>;
    photoId: number;

    constructor(
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private photoService: PhotoService,
        private alertService: AlertService,
        private userService: UserService
        //private notificationService:NotificationService
    ) { }
    
    

    ngOnInit(): void {
        this.photoId = this.activatedRoute.snapshot.params.photoId;
        this.photo$ = this.photoService.findById(this.photoId);
        this.subs.add(
            this.photo$
                .subscribe(
                    () => {}, 
                    err => {
                        console.log(err);
                        this.router.navigate(['not-found']);
                    }
                )
        );
    }

    delete() {
        this.subs.add(
            this.photoService
                .deletePhoto(this.photoId)
                .subscribe(() => {
                    this.alertService.info('Photo Removed Successfully', true);
                    this.router.navigate(['/user', this.userService.getUsername()]);
                }, 
                err => this.alertService.danger(err.error.message)
            )
        )
    }

    ngOnDestroy(): void {
        this.subs.unsubscribe();
    }
}
