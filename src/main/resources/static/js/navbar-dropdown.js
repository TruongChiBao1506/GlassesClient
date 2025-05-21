// Cải thiện hiệu ứng dropdown menu
document.addEventListener('DOMContentLoaded', function() {
    // Thêm hiệu ứng stagger cho dropdown items
    var dropdowns = document.querySelectorAll('.dropdown-menu');
    
    dropdowns.forEach(function(dropdown) {
        var items = dropdown.querySelectorAll('.dropdown-item');
        items.forEach(function(item, index) {
            item.style.animationDelay = (0.1 * (index + 1)) + 's';
        });
    });

    // Xử lý dropdown menu trong navbar - hiện dropdown khi hover
    var navbarDropdownToggles = document.querySelectorAll('#mainNav .dropdown-toggle');
    
    navbarDropdownToggles.forEach(function(toggle) {
        toggle.addEventListener('mouseenter', function() {
            var parent = this.closest('.dropdown') || this.closest('.nav-item');
            if (parent) {
                parent.classList.add('show');
                var menu = parent.querySelector('.dropdown-menu');
                if (menu) menu.classList.add('show');
            }
        });
    });

    // Xử lý sự kiện mouseout cho dropdown trong navbar
    var navItems = document.querySelectorAll('.nav-item.dropdown');
    
    navItems.forEach(function(item) {
        item.addEventListener('mouseleave', function() {
            setTimeout(function() {
                if (!item.matches(':hover')) {
                    item.classList.remove('show');
                    var menu = item.querySelector('.dropdown-menu');
                    if (menu) menu.classList.remove('show');
                }
            }, 200);
        });
    });
    
    // Xử lý riêng cho account dropdown - chỉ hiển thị khi click
    var userDropdown = document.querySelector('.myNavBar__right .dropdown-toggle');
    if (userDropdown) {
        var userParent = userDropdown.closest('.dropdown');
        
        // Chỉ toggle dropdown khi click
        userDropdown.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            var menu = userParent.querySelector('.dropdown-menu');
            if (menu) {
                menu.classList.toggle('show');
            }
        });
        
        // Đóng menu account khi click ra ngoài
        document.addEventListener('click', function(e) {
            var userMenu = userParent.querySelector('.dropdown-menu');
            if (userMenu && userMenu.classList.contains('show') && !userParent.contains(e.target)) {
                userMenu.classList.remove('show');
            }
        });
    }
});
