import { Directive, ElementRef, Input, OnDestroy, OnInit, Renderer2 } from '@angular/core';
import { LocalCacheService } from 'src/app/core/user/local-cache.service';
import { Photo } from 'src/app/model/photo';
import { Comment } from 'src/app/model/comment';
import { SubSink } from 'subsink';

@Directive({
  selector: '[commentOwnerOnly]'
})
export class CommentOwnerOnlyDirective implements OnInit, OnDestroy {

    private subs = new SubSink();
    @Input() ownedComment: Comment;
    @Input() ownedPhoto: Photo;

    constructor(
        private element: ElementRef<any>,
        private renderer: Renderer2,
        private localCacheService: LocalCacheService
    ) { }

    ngOnInit(): void {
        this.subs.add(
            this.localCacheService
                .getUser()
                .subscribe(user => {
                    if(!user || (user.username != this.ownedPhoto.username && user.username != this.ownedComment.username)) {
                        this.renderer.setStyle(this.element.nativeElement, 'display', 'none');
                    }
                })
        );
    }

    ngOnDestroy(): void {
        this.subs.unsubscribe();
    }
  

}
