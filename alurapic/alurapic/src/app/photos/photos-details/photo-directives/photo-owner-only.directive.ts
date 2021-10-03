import { Directive, ElementRef, Input, OnDestroy, OnInit, Renderer2 } from '@angular/core';

import { SubSink } from 'subsink';

import { UserService } from 'src/app/core/user/user.service';
import { Photo } from 'src/app/model/photo';


@Directive({
  selector: '[photoOwnerOnly]'
})
export class PhotoOwnerOnlyDirective implements OnInit, OnDestroy {

    private subs = new SubSink();
    @Input() ownedPhoto: Photo

    constructor(
        private element: ElementRef<any>,
        private renderer: Renderer2,
        private userService: UserService
    ) { }

    ngOnInit(): void {
        this.subs.add(
            this.userService
                .getUser()
                .subscribe(user => {
                    if(!user || user.username != this.ownedPhoto.username) {
                        this.renderer.setStyle(this.element.nativeElement, 'display', 'none');
                    }
                })
        );
    }

    ngOnDestroy(): void {
        this.subs.unsubscribe();
    }

}
