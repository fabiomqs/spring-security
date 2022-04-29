import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from 'src/app/model/user';
import { LocalCacheService } from '../user/local-cache.service';

@Component({
    selector: 'app-footer',
    templateUrl: './footer.component.html'
})
export class FooterComponent implements OnInit {

    user$: Observable<User>;

    constructor(private localCacheService: LocalCacheService) { }

    ngOnInit(): void {
        this.user$ = this.localCacheService.getUser();
    }

}
