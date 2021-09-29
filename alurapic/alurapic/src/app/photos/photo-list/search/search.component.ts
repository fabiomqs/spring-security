import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { Subject } from 'rxjs';
import { debounceTime } from 'rxjs/operators';
import { SubSink } from 'subsink';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html'
})
export class SearchComponent implements OnInit, OnDestroy {

    private subs = new SubSink();

    @Output() onTyping = new EventEmitter<string>();
    @Input() value: string = '';
    debounce: Subject<string> = new Subject<string>();
    
    constructor() { }

    ngOnInit(): void {
        this.subs.add(
            this.debounce
            .pipe(debounceTime(300))
            .subscribe(filter => this.onTyping.emit(filter))
        );
    }

    ngOnDestroy(): void {
        this.subs.unsubscribe();
    }

}
