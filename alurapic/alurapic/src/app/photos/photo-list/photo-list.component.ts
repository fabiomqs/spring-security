import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PAGESIZE } from 'src/app/core/tokens';

import { SubSink } from 'subsink';

import { Photo } from '../../model/photo';
import { PhotoService } from '../service/photo.service';

@Component({
    selector: 'app-photo-list',
    templateUrl: './photo-list.component.html'
})
export class PhotoListComponent implements OnInit, OnDestroy {

    private subs = new SubSink();

    photos:Photo[] = [];
    filter:string = '';
    
    hasMore: boolean = false;
    currentePage: number = 0;
    userName: string = '';


    constructor(
        private photoService: PhotoService,
        private activatedRoute: ActivatedRoute,
        @Inject(PAGESIZE) private pageSize: number
    ) { }
 
    ngOnInit(): void {
        this.subs.add (
            this.activatedRoute.params.subscribe(params => {
                this.userName = params.username;
                this.photos = this.activatedRoute.snapshot.data['photos'];
            })
        );
        
        if(this.photos.length < this.pageSize) {
            this.hasMore = false;
        } else {
            this.hasMore = true;
        }
        
    }

    loadMore() {
        this.subs.add(
            this.photoService
                .listFromUserPaginated(this.userName, ++this.currentePage, this.pageSize)
                .subscribe(photos => {
                    this.filter = '';
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
