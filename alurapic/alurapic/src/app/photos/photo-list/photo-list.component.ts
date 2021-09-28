import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { SubSink } from 'subsink';
import { Photo } from '../photo/photo';

import { PhotoService } from '../photo/photo.service';

@Component({
  selector: 'app-photo-list',
  templateUrl: './photo-list.component.html',
  styleUrls: ['./photo-list.component.css']
})
export class PhotoListComponent implements OnInit, OnDestroy {

    private subs = new SubSink();

    photos:Photo[] = [];
    filter:string = '';

    constructor(
        private photoService:PhotoService,
        private activatedRoute: ActivatedRoute
    ) { }
 
    ngOnInit(): void {
        const username = this.activatedRoute.snapshot.params.username;
        const dest = this.activatedRoute.snapshot.params.dest;
        this.subs.add(
            this.photoService.listFromUser(username)
                .subscribe(
                    photos => {
                        this.photos = photos
                        
                    },
                   err => console.log(err))
        )
    }
    
    ngOnDestroy(): void {
        this.subs.unsubscribe();
    }

}
