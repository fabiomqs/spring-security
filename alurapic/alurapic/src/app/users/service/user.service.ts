import { HttpClient, HttpParams } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { APIURL } from 'src/app/core/tokens';
import { LocalCacheService } from 'src/app/core/user/local-cache.service';
import { User } from 'src/app/model/user';

@Injectable({
    providedIn: 'root'
})
export class UserService {

    constructor(
        private http:HttpClient,
        private localCacheService: LocalCacheService,
        @Inject(APIURL) private apiUrl: string
    ) { }

    listUsers(page: number, size:number):Observable<User[]> {
        const params = new HttpParams()
            .append('page', page.toString())
            .append('size', size.toString());
        return this.http
            .get<User[]>(`${this.apiUrl}/api/v1/user/list`, { params })
    }
    
    addUser(user: User):Observable<User> {
        const formData = new FormData();
        formData.append('firstName', user.firstName);
        formData.append('lastName', user.lastName);
        formData.append('username', user.username);
        formData.append('email', user.email);
        formData.append('role', user.role);
        
        return this.http
            .post<User>(`${this.apiUrl}/api/v1/user/add`, formData);
    }

    updateUser(currentUsername: string, user: User):Observable<User> {
        const formData = new FormData();

        formData.append('currentUsername', currentUsername);
        formData.append('firstName', user.firstName);
        formData.append('lastName', user.lastName);
        formData.append('username', user.username);
        formData.append('email', user.email);
        formData.append('role', user.role);

        formData.append('isNonLocked', user.accountNonLocked ? 'true' : 'false');
        formData.append('isActive', user.active ? 'true' : 'false');
        formData.append('NotExpired', user.NotExpired ? 'true' : 'false');
        formData.append('credentialsNotExpired', user.credentialsNotExpired ? 'true' : 'false');
        formData.append('suspended', user.suspended ? 'true' : 'false');
        formData.append('banned', user.banned ? 'true' : 'false');
        
        return this.http
            .post<User>(`${this.apiUrl}/api/v1/user/add`, formData);
    }

    findUser(username: string):Observable<User[]> {
        return this.http
            .get<User[]>(`${this.apiUrl}/api/v1/user/find/${username}`)
    }

    suspendUser(username: string):Observable<User> {
        return this.http
            .put<User>(`${this.apiUrl}/api/v1/user/suspend/${username}`, {})
    }

    banUser(username: string):Observable<User> {
        return this.http
            .put<User>(`${this.apiUrl}/api/v1/user/ban/${username}`, {})
    }

    deleteUser(username: string) {
        return this.http
            .delete(`${this.apiUrl}/api/v1/user/ban/${username}`)
    }

    updateProfileImage() {

    }

}
