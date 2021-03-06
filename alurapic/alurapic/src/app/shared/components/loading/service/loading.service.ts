import { Injectable } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';
import { startWith } from 'rxjs/operators';
import { LoadingType } from 'src/app/enums/loading-type.enum';

@Injectable({
    providedIn: 'root'
})
export class LoadingService {

    loadingSubject = new Subject<LoadingType>();

    constructor() { }

    getLoading() {
        return this.loadingSubject
            .asObservable()
            .pipe(startWith(LoadingType.STOPPED));
    }

    start() {
        this.loadingSubject.next(LoadingType.LOADING);
    }
    
    stop() {
        this.loadingSubject.next(LoadingType.STOPPED);
    }
}
