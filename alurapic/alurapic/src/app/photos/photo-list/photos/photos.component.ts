import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { Photo } from '../../../model/photo';

@Component({
    selector: 'app-photos',
    templateUrl: './photos.component.html'
})
export class PhotosComponent implements OnInit, OnChanges {

    @Input() photos:Photo[] = [];
    rows: any[] = [];

    constructor() {}
    
    ngOnInit(): void {
        
    }
    
    ngOnChanges(changes: SimpleChanges): void {
        if(changes.photos)
            this.rows = this.groupColuns(this.photos);
    }
    private groupColuns(photos: Photo[]): any[] {
        const newRows = [];

        for(let index = 0; index < photos.length; index += 3) {
            newRows.push(photos.slice(index, index + 3));
        }

        return newRows;
    }

}
