// Powered by OnSpace.AI
import React from 'react';
import { View, Text, StyleSheet, Pressable, Switch } from 'react-native';
import { MaterialIcons } from '@expo/vector-icons';
import { SpoofProfile } from '@/services/profileService';
import { Colors, FontSize, FontWeight, Radius, Spacing } from '@/constants/theme';

interface Props {
  profile:  SpoofProfile;
  onEdit:   (p: SpoofProfile) => void;
  onDelete: (id: string) => void;
  onToggle: (id: string) => void;
}

export const ProfileCard = React.memo(({ profile, onEdit, onDelete, onToggle }: Props) => {
  return (
    <Pressable
      onPress={() => onEdit(profile)}
      style={({ pressed }) => [styles.card, pressed && styles.pressed, profile.enabled && styles.cardActive]}
    >
      {/* Header */}
      <View style={styles.header}>
        <View style={styles.nameRow}>
          <View style={[styles.dot, profile.enabled ? styles.dotOn : styles.dotOff]} />
          <Text style={styles.name} numberOfLines={1}>{profile.name}</Text>
        </View>
        <View style={styles.actions}>
          <Pressable onPress={() => onDelete(profile.id)} hitSlop={10} style={styles.iconBtn}>
            <MaterialIcons name="delete-outline" size={18} color={Colors.danger} />
          </Pressable>
          <Switch
            value={profile.enabled}
            onValueChange={() => onToggle(profile.id)}
            trackColor={{ false: Colors.border, true: Colors.primaryDim }}
            thumbColor={profile.enabled ? Colors.primary : Colors.textMuted}
            style={styles.switch}
          />
        </View>
      </View>

      {/* Package */}
      <Text style={styles.pkg} numberOfLines={1}>
        <Text style={styles.pkgLabel}>PKG </Text>
        {profile.targetPackage || 'All apps'}
      </Text>

      {/* Info grid */}
      <View style={styles.grid}>
        <InfoChip label="IPv4" value={profile.ipv4} />
        <InfoChip label="IPv6" value={profile.ipv6} />
        <InfoChip label="MFR"  value={profile.manufacturer} />
        <InfoChip label="MDL"  value={profile.model} />
        <InfoChip label="AID"  value={profile.androidId} />
        <InfoChip label="SDK"  value={`API ${profile.sdkInt}`} />
      </View>
    </Pressable>
  );
});

function InfoChip({ label, value }: { label: string; value: string }) {
  return (
    <View style={chipStyles.wrap}>
      <Text style={chipStyles.label}>{label}</Text>
      <Text style={chipStyles.value} numberOfLines={1}>{value}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  card: {
    backgroundColor: Colors.bgCard,
    borderRadius: Radius.lg,
    padding: Spacing.md,
    marginBottom: Spacing.md,
    borderWidth: 1,
    borderColor: Colors.border,
  },
  cardActive: { borderColor: Colors.primaryDim },
  pressed:    { opacity: 0.85 },
  header:     { flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', marginBottom: Spacing.sm },
  nameRow:    { flexDirection: 'row', alignItems: 'center', gap: 8, flex: 1 },
  dot:        { width: 8, height: 8, borderRadius: 4 },
  dotOn:      { backgroundColor: Colors.primary },
  dotOff:     { backgroundColor: Colors.textMuted },
  name:       { fontSize: FontSize.md, fontWeight: FontWeight.bold, color: Colors.textPrimary, flex: 1 },
  actions:    { flexDirection: 'row', alignItems: 'center', gap: 8 },
  iconBtn:    { padding: 4 },
  switch:     { transform: [{ scaleX: 0.85 }, { scaleY: 0.85 }] },
  pkg:        { fontSize: FontSize.xs, color: Colors.textMuted, marginBottom: Spacing.sm, fontFamily: 'monospace' },
  pkgLabel:   { color: Colors.textLabel },
  grid:       { flexDirection: 'row', flexWrap: 'wrap', gap: 6 },
});

const chipStyles = StyleSheet.create({
  wrap:  { backgroundColor: Colors.bgHighlight, borderRadius: Radius.sm, paddingHorizontal: 8, paddingVertical: 4, minWidth: 90 },
  label: { fontSize: 9, color: Colors.textLabel, fontWeight: FontWeight.bold, letterSpacing: 1 },
  value: { fontSize: FontSize.xs, color: Colors.primary, fontFamily: 'monospace' },
});
