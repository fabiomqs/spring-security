import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { LocalCacheService } from '../../user/local-cache.service';

@Injectable({
    providedIn: 'root'
})
export class AuthGuard implements CanActivate {

    constructor(
        private localCacheService:LocalCacheService,
        private router:Router
    ) {}

    canActivate(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
            if(!this.localCacheService.isLogged()) {
                this.router.navigate(
                    [''],
                    {
                        queryParams: {
                            fromUrl: state.url
                        }
                    }
                );
                return false;
            }
            return true;
    }
  
}
