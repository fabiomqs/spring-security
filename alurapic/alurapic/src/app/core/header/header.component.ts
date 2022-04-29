import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { User } from 'src/app/model/user';
import { LocalCacheService } from '../user/local-cache.service';

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html'
})
export class HeaderComponent implements OnInit {

    user$: Observable<User>

    constructor(
        private localCacheService: LocalCacheService,
        private router:Router
    ) { }

    ngOnInit(): void {
        this.user$ = this.localCacheService.getUser();
    }

    logout() {
        this.localCacheService.logout();
        this.router.navigate(['']);
    }

}
