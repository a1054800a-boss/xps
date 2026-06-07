// Powered by OnSpace.AI
import React from 'react';
import { Pressable, Text, StyleSheet, ViewStyle, TextStyle } from 'react-native';
import { Colors, Radius, FontSize, FontWeight, Spacing } from '@/constants/theme';

interface Props {
  label:     string;
  onPress:   () => void;
  variant?:  'primary' | 'danger' | 'accent' | 'ghost';
  size?:     'sm' | 'md' | 'lg';
  style?:    ViewStyle;
  textStyle?: TextStyle;
  disabled?: boolean;
}

export const GlowButton = React.memo(({ label, onPress, variant = 'primary', size = 'md', style, textStyle, disabled }: Props) => {
  const bg    = variant === 'primary' ? Colors.primary
              : variant === 'danger'  ? Colors.danger
              : variant === 'accent'  ? Colors.accent
              : 'transparent';
  const txtC  = variant === 'ghost' ? Colors.textSecondary : Colors.bg;
  const border= variant === 'ghost' ? Colors.border : bg;

  const heights: Record<string, number> = { sm: 36, md: 44, lg: 52 };
  const fontSizes: Record<string, number> = { sm: FontSize.xs, md: FontSize.sm, lg: FontSize.md };

  return (
    <Pressable
      onPress={onPress}
      disabled={disabled}
      style={({ pressed }) => [
        styles.btn,
        { backgroundColor: bg, borderColor: border, height: heights[size], opacity: pressed || disabled ? 0.65 : 1 },
        style,
      ]}
    >
      <Text style={[styles.text, { color: txtC, fontSize: fontSizes[size] }, textStyle]}>
        {label}
      </Text>
    </Pressable>
  );
});

const styles = StyleSheet.create({
  btn: {
    borderRadius: Radius.md,
    borderWidth: 1,
    alignItems: 'center',
    justifyContent: 'center',
    paddingHorizontal: Spacing.md,
  },
  text: {
    fontWeight: FontWeight.bold,
    letterSpacing: 0.5,
  },
});
