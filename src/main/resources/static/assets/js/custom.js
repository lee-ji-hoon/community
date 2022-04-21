/* ----------------- Start Document ----------------- */
(function($){
    "use strict";
    
    $(document).ready(function(){
         
        /*----------------------------------------------------*/
        /* Sidebar Nav submenu
        /*----------------------------------------------------*/
    
        $('.sidebar_inner ul li a').on('click', function(e){
            if($(this).closest("li").children("ul").length) {
                if ( $(this).closest("li").is(".active-submenu") ) {
                   $('.sidebar_inner ul li').removeClass('active-submenu');
                } else {
                    $('.sidebar_inner ul li').removeClass('active-submenu');
                    $(this).parent('li').addClass('active-submenu');
                }
                e.preventDefault();
            }
        });

        /*--------------------------------------------------*/
        /*  Tippy JS 
        /*--------------------------------------------------*/
        /* global tippy */
        tippy('[data-tippy-placement]', {
            delay: 100,
            arrow: true,
            arrowType: 'sharp',
            size: 'regular',
            duration: 200,
    
            // 'shift-toward', 'fade', 'scale', 'perspective'
            animation: 'shift-away',
    
            animateFill: true,
            theme: 'dark',
    
            // How far the tooltip is from its reference element in pixels 
            distance: 10,
    
        });
    

    
    // ------------------ End Document ------------------ //
    });
    
    })(this.jQuery);
    

    // On page load or when changing themes, best to add inline in `head` to avoid FOUC
    if (localStorage.theme === 'dark' || (!('theme' in localStorage) && window.matchMedia('(prefers-color-scheme: dark)').matches)) {
        document.documentElement.classList.add('dark')
    } else {
        document.documentElement.classList.remove('dark')
    }

    // Whenever the user explicitly chooses light mode
    localStorage.theme = 'light'

    // Whenever the user explicitly chooses dark mode
    localStorage.theme = 'dark'

    // Whenever the user explicitly chooses to respect the OS preference
    localStorage.removeItem('theme')