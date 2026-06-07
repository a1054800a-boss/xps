// Powered by OnSpace.AI
export const Colors = {
  // Base
  bg:           '#080d08',
  bgCard:       '#0f1a0f',
  bgInput:      '#111911',
  bgHighlight:  '#162016',
  border:       '#1e2e1e',
  borderActive: '#00ff88',

  // Brand
  primary:      '#00ff88',
  primaryDim:   '#00cc6a',
  primaryGlow:  'rgba(0,255,136,0.15)',
  accent:       '#00d4ff',
  accentDim:    '#00aacc',
  accentGlow:   'rgba(0,212,255,0.12)',

  // Semantic
  danger:       '#ff4444',
  dangerGlow:   'rgba(255,68,68,0.15)',
  warning:      '#ffaa00',
  success:      '#00ff88',

  // Text
  textPrimary:  '#e8ffe8',
  textSecondary:'#7aaa7a',
  textMuted:    '#3d5c3d',
  textLabel:    '#5a8a5a',
};

export const Spacing = {
  xs: 4,
  sm: 8,
  md: 16,
  lg: 24,
  xl: 32,
  xxl: 48,
};

export const Radius = {
  sm: 6,
  md: 10,
  lg: 16,
  xl: 24,
  pill: 100,
};

export const FontSize = {
  xs:   11,
  sm:   13,
  md:   16,
  lg:   18,
  xl:   20,
  xxl:  24,
  hero: 28,
};

export const FontWeight = {
  regular: '400' as const,
  medium:  '500' as const,
  semibold:'600' as const,
  bold:    '700' as const,
};
