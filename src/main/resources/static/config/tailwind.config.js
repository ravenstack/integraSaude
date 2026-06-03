tailwind.config = {
    darkMode: "class",
    theme: {
        extend: {
            colors: {
                // Cores primárias
                "primary": "#7543a9",
                "amethyst": "#925fc7",
                "secondary": "#6b567f",

                // Cores de fundo
                "background": "#fff7fe",
                "surface": "#ffffff",

                // Variações de superfície
                "surface-container-low": "#faf1fa",
                "surface-container": "#f4ebf4",
                "surface-container-high": "#eee5ef",
                "surface-container-highest": "#e8e0e9",
                "surface-container-lowest": "#ffffff",
                "surface-variant": "#e8e0e9",

                // Estados
                "primary-fixed": "#f0dbff",
                "primary-fixed-dim": "#eaddff",
                "primary-container": "#8f5cc4",

                // Texto
                "on-primary": "#ffffff",
                "on-primary-container": "#fffbff",
                "on-surface": "#1e1a21",
                "on-surface-variant": "#4b4451",
                "on-background": "#1e1a21",

                // Cores de estado
                "outline": "#7d7482",
                "outline-variant": "#cec3d3",
                "error": "#ba1a1a",
                "error-container": "#ffdad6",
                "on-error-container": "#93000a",

                // Sucesso
                "success": "#0f5223",
                "success-container": "#c8e6c9",
                "on-success-container": "#002107"
            },
            fontFamily: {
                "sans": ["Outfit", "sans-serif"],
                "serif": ["Outfit", "sans-serif"],
                "label-caps": ["Outfit"],
                "body-lg": ["Outfit"],
                "body-md": ["Outfit"],
                "body-sm": ["Outfit"],
                "h1": ["Outfit"],
                "h2": ["Outfit"],
                "h3": ["Outfit"],
                "button": ["Outfit"]
            },
            borderRadius: {
                "DEFAULT": "1rem",
                "sm": "0.5rem",
                "md": "16px",
                "lg": "32px",
                "xl": "48px",
                "full": "9999px"
            },
            spacing: {
                "xs": "8px",
                "sm": "16px",
                "md": "24px",
                "lg": "32px",
                "xl": "48px",
                "base": "4px",
                "margin": "24px"
            }
        }
    }
}