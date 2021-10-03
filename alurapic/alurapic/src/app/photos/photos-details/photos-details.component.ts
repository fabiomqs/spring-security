import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { NotificationService } from 'src/app/core/notification/service/notification.service';
import { Photo } from 'src/app/model/photo';
import { PhotoService } from '../photo/service/photo.service';

@Component({
    templateUrl: './photos-details.component.html',
    styleUrls: ['photo-details.css']
})
export class PhotosDetailsComponent implements OnInit{

    photo$: Observable<Photo>;

    constructor(
        private activatedRoute:ActivatedRoute,
        private photoService:PhotoService,
        private notificationService:NotificationService
    ) { }
    

    ngOnInit(): void {
        this.photo$ = this.photoService
            .findById(this.activatedRoute.snapshot.params.photoId);
    }
}
