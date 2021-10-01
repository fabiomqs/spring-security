import { Inject, Injectable } from '@angular/core';
import { Resolve, RouterStateSnapshot, ActivatedRouteSnapshot } from '@angular/router';
import { Observable, of } from 'rxjs';
import { PAGESIZE } from 'src/app/core/tokens';

import { Photo } from '../photo/photo';
import { PhotoService } from '../photo/photo.service';

@Injectable({
    providedIn: 'root'
})
export class PhotoListResolver implements Resolve<Observable<Photo[]>> {

    constructor(
        private photoService: PhotoService,
        @Inject(PAGESIZE) private pageSize: number
    ) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Photo[]> {
        const userName = route.params.username;
        return this.photoService.listFromUserPaginated(userName, 0, this.pageSize);
    }
}
