import { Component, Input, OnDestroy, OnInit } from '@angular/core';

@Component({
    selector: 'app-photo',
    templateUrl: './photo.component.html'
})
export class PhotoComponent implements OnInit, OnDestroy {

    @Input() description='';
    @Input() url='';

    constructor() { }
    
    ngOnInit(): void {
    }

    ngOnDestroy(): void {    
    }

}
