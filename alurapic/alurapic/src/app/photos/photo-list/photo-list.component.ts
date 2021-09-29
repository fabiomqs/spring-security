import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subject } from 'rxjs';
import { debounceTime } from 'rxjs/operators';

import { SubSink } from 'subsink';

import { Photo } from '../photo/photo';
import { PhotoService } from '../photo/photo.service';

@Component({
  selector: 'app-photo-list',
  templateUrl: './photo-list.component.html'
})
export class PhotoListComponent implements OnInit, OnDestroy {

    private subs = new SubSink();

    photos:Photo[] = [];
    filter:string = '';
    debounce: Subject<string> = new Subject<string>();
    hasMore: boolean = false;
    currentePage: number = 0;
    //TODO - put page size on enviroments
    pageSize: number = 12;
    userName: string = '';


    constructor(
        private photoService: PhotoService,
        private activatedRoute: ActivatedRoute
    ) { }
 
    ngOnInit(): void {
        this.userName = this.activatedRoute.snapshot.params.username;
        this.photos = this.activatedRoute.snapshot.data['photos'];
        if(this.photos.length < this.pageSize) {
            this.hasMore = false;
        } else {
            this.hasMore = true;
        }
        this.subs.add(
            this.debounce
            .pipe(debounceTime(300))
            .subscribe(filter => this.filter = filter)
        );
    }

    loadMore() {
        this.subs.add(
            this.photoService
                .listFromUserPaginated(this.userName, ++this.currentePage, this.pageSize)
                .subscribe(photos => {
                    this.photos = this.photos.concat(photos);
                    if(photos.length < this.pageSize)
                        this.hasMore = false;
                }, 
                error => console.log(error))
        );
    }
    
    ngOnDestroy(): void {
        this.subs.unsubscribe();
    }

}
