// Powered by OnSpace.AI
import React, { useState } from 'react';
import { View, Text, TextInput, StyleSheet, Pressable } from 'react-native';
import { MaterialIcons } from '@expo/vector-icons';
import { Colors, FontSize, FontWeight, Radius, Spacing } from '@/constants/theme';

interface Props {
  label:       string;
  value:       string;
  onChange:    (v: string) => void;
  placeholder?:string;
  monospace?:  boolean;
  onRandomize?:() => void;
  error?:      string;
  multiline?:  boolean;
}

export const FieldInput = React.memo(({ label, value, onChange, placeholder, monospace, onRandomize, error, multiline }: Props) => {
  const [focused, setFocused] = useState(false);

  return (
    <View style={styles.wrapper}>
      <View style={styles.labelRow}>
        <Text style={styles.label}>{label}</Text>
        {onRandomize ? (
          <Pressable onPress={onRandomize} style={styles.rndBtn} hitSlop={8}>
            <MaterialIcons name="shuffle" size={14} color={Colors.accent} />
            <Text style={styles.rndText}>RND</Text>
          </Pressable>
        ) : null}
      </View>
      <View style={[styles.inputWrap, focused && styles.focused, error ? styles.errBorder : null]}>
        <TextInput
          style={[styles.input, monospace && styles.mono, multiline && styles.multiline]}
          value={value}
          onChangeText={onChange}
          placeholder={placeholder ?? label}
          placeholderTextColor={Colors.textMuted}
          onFocus={() => setFocused(true)}
          onBlur={() => setFocused(false)}
          autoCapitalize="none"
          autoCorrect={false}
          multiline={multiline}
          numberOfLines={multiline ? 3 : 1}
        />
      </View>
      {error ? <Text style={styles.errText}>{error}</Text> : null}
    </View>
  );
});

const styles = StyleSheet.create({
  wrapper:  { marginBottom: 12 },
  labelRow: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 4 },
  label:    { fontSize: FontSize.xs, fontWeight: FontWeight.semibold, color: Colors.textLabel, letterSpacing: 1, textTransform: 'uppercase' },
  rndBtn:   { flexDirection: 'row', alignItems: 'center', gap: 2 },
  rndText:  { fontSize: FontSize.xs, color: Colors.accent, fontWeight: FontWeight.bold },
  inputWrap:{ borderWidth: 1, borderColor: Colors.border, borderRadius: Radius.md, backgroundColor: Colors.bgInput, paddingHorizontal: Spacing.md },
  focused:  { borderColor: Colors.borderActive },
  errBorder:{ borderColor: Colors.danger },
  input:    { color: Colors.textPrimary, fontSize: FontSize.sm, height: 44 },
  mono:     { fontFamily: 'monospace', fontSize: FontSize.xs },
  multiline:{ height: 70, textAlignVertical: 'top', paddingTop: 10 },
  errText:  { fontSize: FontSize.xs, color: Colors.danger, marginTop: 2 },
});
