import { HttpClient, HttpResponse } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { HeaderType } from 'src/app/enums/header-type.enum';
import { User } from 'src/app/model/user';
import { APIURL } from '../../tokens';
import { LocalCacheService } from '../../user/local-cache.service';

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    constructor(
        private httpClient:HttpClient,
        private localCacheService:LocalCacheService,
        @Inject(APIURL) private apiUrl: string
    ) { }

    authenticate(user: User): Observable<HttpResponse<User>> {
        return this.httpClient.post<User>(
            `${this.apiUrl}/login`, user, {observe: 'response'})
            .pipe(tap(
                (response: HttpResponse<User>) => {
                    const token = response.headers.get(HeaderType.JWT_TOKEN);
                    this.localCacheService.setToken(token);
                }
            ));
    }

}
