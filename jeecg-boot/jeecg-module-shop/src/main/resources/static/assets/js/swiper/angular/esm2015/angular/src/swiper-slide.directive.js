import { Directive, Input } from '@angular/core';
import { coerceBooleanProperty } from './utils/utils';
import * as i0 from "@angular/core";
export class SwiperSlideDirective {
    constructor(template) {
        this.template = template;
        this.class = '';
        this.autoplayDelay = null;
        this.slideData = {
            isActive: false,
            isPrev: false,
            isNext: false,
            isVisible: false,
            isDuplicate: false,
        };
    }
    set zoom(val) {
        this._zoom = coerceBooleanProperty(val);
    }
    get zoom() {
        return this._zoom;
    }
    get classNames() {
        return this._classNames;
    }
    set classNames(val) {
        if (this._classNames === val) {
            return;
        }
        this._classNames = val;
        this.slideData = {
            isActive: this._hasClass(['swiper-slide-active', 'swiper-slide-duplicate-active']),
            isVisible: this._hasClass(['swiper-slide-visible']),
            isDuplicate: this._hasClass(['swiper-slide-duplicate']),
            isPrev: this._hasClass(['swiper-slide-prev', 'swiper-slide-duplicate-prev']),
            isNext: this._hasClass(['swiper-slide-next', 'swiper-slide-duplicate-next']),
        };
    }
    _hasClass(classNames) {
        return classNames.some((className) => this._classNames.indexOf(className) >= 0);
    }
}
SwiperSlideDirective.ɵfac = i0.ɵɵngDeclareFactory({ minVersion: "12.0.0", version: "12.2.2", ngImport: i0, type: SwiperSlideDirective, deps: [{ token: i0.TemplateRef }], target: i0.ɵɵFactoryTarget.Directive });
SwiperSlideDirective.ɵdir = i0.ɵɵngDeclareDirective({ minVersion: "12.0.0", version: "12.2.2", type: SwiperSlideDirective, selector: "ng-template[swiperSlide]", inputs: { virtualIndex: "virtualIndex", class: "class", autoplayDelay: ["data-swiper-autoplay", "autoplayDelay"], zoom: "zoom" }, ngImport: i0 });
i0.ɵɵngDeclareClassMetadata({ minVersion: "12.0.0", version: "12.2.2", ngImport: i0, type: SwiperSlideDirective, decorators: [{
            type: Directive,
            args: [{
                    selector: 'ng-template[swiperSlide]',
                }]
        }], ctorParameters: function () { return [{ type: i0.TemplateRef }]; }, propDecorators: { virtualIndex: [{
                type: Input
            }], class: [{
                type: Input
            }], autoplayDelay: [{
                type: Input,
                args: ['data-swiper-autoplay']
            }], zoom: [{
                type: Input
            }] } });
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoic3dpcGVyLXNsaWRlLmRpcmVjdGl2ZS5qcyIsInNvdXJjZVJvb3QiOiIiLCJzb3VyY2VzIjpbIi4uLy4uLy4uLy4uLy4uL3NyYy9hbmd1bGFyL3NyYy9zd2lwZXItc2xpZGUuZGlyZWN0aXZlLnRzIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiJBQUFBLE9BQU8sRUFBRSxTQUFTLEVBQUUsS0FBSyxFQUFlLE1BQU0sZUFBZSxDQUFDO0FBQzlELE9BQU8sRUFBRSxxQkFBcUIsRUFBRSxNQUFNLGVBQWUsQ0FBQzs7QUFJdEQsTUFBTSxPQUFPLG9CQUFvQjtJQTBDL0IsWUFBbUIsUUFBMEI7UUFBMUIsYUFBUSxHQUFSLFFBQVEsQ0FBa0I7UUF4Q3BDLFVBQUssR0FBVyxFQUFFLENBQUM7UUFDRyxrQkFBYSxHQUFrQixJQUFJLENBQUM7UUE4Qm5FLGNBQVMsR0FBRztZQUNWLFFBQVEsRUFBRSxLQUFLO1lBQ2YsTUFBTSxFQUFFLEtBQUs7WUFDYixNQUFNLEVBQUUsS0FBSztZQUNiLFNBQVMsRUFBRSxLQUFLO1lBQ2hCLFdBQVcsRUFBRSxLQUFLO1NBQ25CLENBQUM7SUFHOEMsQ0FBQztJQXRDakQsSUFDSSxJQUFJLENBQUMsR0FBWTtRQUNuQixJQUFJLENBQUMsS0FBSyxHQUFHLHFCQUFxQixDQUFDLEdBQUcsQ0FBQyxDQUFDO0lBQzFDLENBQUM7SUFDRCxJQUFJLElBQUk7UUFDTixPQUFPLElBQUksQ0FBQyxLQUFLLENBQUM7SUFDcEIsQ0FBQztJQUdELElBQUksVUFBVTtRQUNaLE9BQU8sSUFBSSxDQUFDLFdBQVcsQ0FBQztJQUMxQixDQUFDO0lBQ0QsSUFBSSxVQUFVLENBQUMsR0FBRztRQUNoQixJQUFJLElBQUksQ0FBQyxXQUFXLEtBQUssR0FBRyxFQUFFO1lBQzVCLE9BQU87U0FDUjtRQUNELElBQUksQ0FBQyxXQUFXLEdBQUcsR0FBRyxDQUFDO1FBQ3ZCLElBQUksQ0FBQyxTQUFTLEdBQUc7WUFDZixRQUFRLEVBQUUsSUFBSSxDQUFDLFNBQVMsQ0FBQyxDQUFDLHFCQUFxQixFQUFFLCtCQUErQixDQUFDLENBQUM7WUFDbEYsU0FBUyxFQUFFLElBQUksQ0FBQyxTQUFTLENBQUMsQ0FBQyxzQkFBc0IsQ0FBQyxDQUFDO1lBQ25ELFdBQVcsRUFBRSxJQUFJLENBQUMsU0FBUyxDQUFDLENBQUMsd0JBQXdCLENBQUMsQ0FBQztZQUN2RCxNQUFNLEVBQUUsSUFBSSxDQUFDLFNBQVMsQ0FBQyxDQUFDLG1CQUFtQixFQUFFLDZCQUE2QixDQUFDLENBQUM7WUFDNUUsTUFBTSxFQUFFLElBQUksQ0FBQyxTQUFTLENBQUMsQ0FBQyxtQkFBbUIsRUFBRSw2QkFBNkIsQ0FBQyxDQUFDO1NBQzdFLENBQUM7SUFDSixDQUFDO0lBRU8sU0FBUyxDQUFDLFVBQW9CO1FBQ3BDLE9BQU8sVUFBVSxDQUFDLElBQUksQ0FBQyxDQUFDLFNBQVMsRUFBRSxFQUFFLENBQUMsSUFBSSxDQUFDLFdBQVcsQ0FBQyxPQUFPLENBQUMsU0FBUyxDQUFDLElBQUksQ0FBQyxDQUFDLENBQUM7SUFDbEYsQ0FBQzs7aUhBaENVLG9CQUFvQjtxR0FBcEIsb0JBQW9COzJGQUFwQixvQkFBb0I7a0JBSGhDLFNBQVM7bUJBQUM7b0JBQ1QsUUFBUSxFQUFFLDBCQUEwQjtpQkFDckM7a0dBRVUsWUFBWTtzQkFBcEIsS0FBSztnQkFDRyxLQUFLO3NCQUFiLEtBQUs7Z0JBQ3lCLGFBQWE7c0JBQTNDLEtBQUs7dUJBQUMsc0JBQXNCO2dCQUV6QixJQUFJO3NCQURQLEtBQUsiLCJzb3VyY2VzQ29udGVudCI6WyJpbXBvcnQgeyBEaXJlY3RpdmUsIElucHV0LCBUZW1wbGF0ZVJlZiB9IGZyb20gJ0Bhbmd1bGFyL2NvcmUnO1xuaW1wb3J0IHsgY29lcmNlQm9vbGVhblByb3BlcnR5IH0gZnJvbSAnLi91dGlscy91dGlscyc7XG5ARGlyZWN0aXZlKHtcbiAgc2VsZWN0b3I6ICduZy10ZW1wbGF0ZVtzd2lwZXJTbGlkZV0nLFxufSlcbmV4cG9ydCBjbGFzcyBTd2lwZXJTbGlkZURpcmVjdGl2ZSB7XG4gIEBJbnB1dCgpIHZpcnR1YWxJbmRleDogbnVtYmVyO1xuICBASW5wdXQoKSBjbGFzczogc3RyaW5nID0gJyc7XG4gIEBJbnB1dCgnZGF0YS1zd2lwZXItYXV0b3BsYXknKSBhdXRvcGxheURlbGF5OiBzdHJpbmcgfCBudWxsID0gbnVsbDtcbiAgQElucHV0KClcbiAgc2V0IHpvb20odmFsOiBib29sZWFuKSB7XG4gICAgdGhpcy5fem9vbSA9IGNvZXJjZUJvb2xlYW5Qcm9wZXJ0eSh2YWwpO1xuICB9XG4gIGdldCB6b29tKCkge1xuICAgIHJldHVybiB0aGlzLl96b29tO1xuICB9XG4gIHByaXZhdGUgX3pvb206IGJvb2xlYW47XG4gIHNsaWRlSW5kZXg6IG51bWJlcjtcbiAgZ2V0IGNsYXNzTmFtZXMoKSB7XG4gICAgcmV0dXJuIHRoaXMuX2NsYXNzTmFtZXM7XG4gIH1cbiAgc2V0IGNsYXNzTmFtZXModmFsKSB7XG4gICAgaWYgKHRoaXMuX2NsYXNzTmFtZXMgPT09IHZhbCkge1xuICAgICAgcmV0dXJuO1xuICAgIH1cbiAgICB0aGlzLl9jbGFzc05hbWVzID0gdmFsO1xuICAgIHRoaXMuc2xpZGVEYXRhID0ge1xuICAgICAgaXNBY3RpdmU6IHRoaXMuX2hhc0NsYXNzKFsnc3dpcGVyLXNsaWRlLWFjdGl2ZScsICdzd2lwZXItc2xpZGUtZHVwbGljYXRlLWFjdGl2ZSddKSxcbiAgICAgIGlzVmlzaWJsZTogdGhpcy5faGFzQ2xhc3MoWydzd2lwZXItc2xpZGUtdmlzaWJsZSddKSxcbiAgICAgIGlzRHVwbGljYXRlOiB0aGlzLl9oYXNDbGFzcyhbJ3N3aXBlci1zbGlkZS1kdXBsaWNhdGUnXSksXG4gICAgICBpc1ByZXY6IHRoaXMuX2hhc0NsYXNzKFsnc3dpcGVyLXNsaWRlLXByZXYnLCAnc3dpcGVyLXNsaWRlLWR1cGxpY2F0ZS1wcmV2J10pLFxuICAgICAgaXNOZXh0OiB0aGlzLl9oYXNDbGFzcyhbJ3N3aXBlci1zbGlkZS1uZXh0JywgJ3N3aXBlci1zbGlkZS1kdXBsaWNhdGUtbmV4dCddKSxcbiAgICB9O1xuICB9XG5cbiAgcHJpdmF0ZSBfaGFzQ2xhc3MoY2xhc3NOYW1lczogc3RyaW5nW10pIHtcbiAgICByZXR1cm4gY2xhc3NOYW1lcy5zb21lKChjbGFzc05hbWUpID0+IHRoaXMuX2NsYXNzTmFtZXMuaW5kZXhPZihjbGFzc05hbWUpID49IDApO1xuICB9XG4gIHNsaWRlRGF0YSA9IHtcbiAgICBpc0FjdGl2ZTogZmFsc2UsXG4gICAgaXNQcmV2OiBmYWxzZSxcbiAgICBpc05leHQ6IGZhbHNlLFxuICAgIGlzVmlzaWJsZTogZmFsc2UsXG4gICAgaXNEdXBsaWNhdGU6IGZhbHNlLFxuICB9O1xuXG4gIHByaXZhdGUgX2NsYXNzTmFtZXM6IHN0cmluZztcbiAgY29uc3RydWN0b3IocHVibGljIHRlbXBsYXRlOiBUZW1wbGF0ZVJlZjxhbnk+KSB7fVxufVxuIl19