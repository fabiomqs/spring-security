import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subject } from 'rxjs';
import { debounceTime } from 'rxjs/operators';

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
    debounce: Subject<string> = new Subject<string>();

    constructor(
        private activatedRoute: ActivatedRoute
    ) { }
 
    ngOnInit(): void {
        this.photos = this.activatedRoute.snapshot.data['photos'];
        this.subs.add(
            this.debounce
            .pipe(debounceTime(300))
            .subscribe(filter => this.filter = filter)
        );
    }
    
    ngOnDestroy(): void {
        this.subs.unsubscribe();
    }

}
