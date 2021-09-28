import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { SubSink } from 'subsink';

import { Photo } from '../photo/photo';

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
        private activatedRoute: ActivatedRoute
    ) { }
 
    ngOnInit(): void {
        this.photos = this.activatedRoute.snapshot.data['photos'];
    }
    
    ngOnDestroy(): void {
        this.subs.unsubscribe();
    }

}
