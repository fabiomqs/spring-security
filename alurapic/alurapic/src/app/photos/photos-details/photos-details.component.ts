import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { Photo } from 'src/app/model/photo';
import { PhotoService } from '../service/photo.service';

@Component({
    templateUrl: './photos-details.component.html'
})
export class PhotosDetailsComponent implements OnInit{

    photo$: Observable<Photo>;
    photoId: number;

    constructor(
        private activatedRoute:ActivatedRoute,
        private photoService:PhotoService
    ) { }
    

    ngOnInit(): void {
        this.photoId = this.activatedRoute.snapshot.params.photoId;

        this.photo$ = this.photoService.findById(this.photoId);
        
        
    }
}
