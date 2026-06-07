// Powered by OnSpace.AI
import React, { useState, useCallback } from 'react';
import {
  View, Text, ScrollView, StyleSheet, Pressable,
  KeyboardAvoidingView, Platform, Modal,
} from 'react-native';
import { MaterialIcons } from '@expo/vector-icons';
import { SpoofProfile, defaultProfile } from '@/services/profileService';
import { generateFullRandom, randomIPv4, randomIPv6, randomAndroidId, randomFingerprint, randomBuildId } from '@/services/randomizer';
import { FieldInput }  from '@/components/ui/FieldInput';
import { GlowButton }  from '@/components/ui/GlowButton';
import { Colors, FontSize, FontWeight, Radius, Spacing } from '@/constants/theme';

interface Props {
  visible:   boolean;
  initial?:  SpoofProfile;
  onSave:    (p: SpoofProfile) => void;
  onCancel:  () => void;
}

function validate(f: Record<string, string>): Record<string, string> {
  const errs: Record<string, string> = {};
  if (!f.name.trim()) errs.name = 'Name is required';
  const ipv4rx = /^(\d{1,3}\.){3}\d{1,3}$/;
  if (f.ipv4 && !ipv4rx.test(f.ipv4)) errs.ipv4 = 'Invalid IPv4';
  if (f.androidId && !/^[0-9a-fA-F]{16}$/.test(f.androidId)) errs.androidId = 'Must be 16 hex chars';
  if (!f.sdkInt || isNaN(Number(f.sdkInt))) errs.sdkInt = 'Must be a number';
  return errs;
}

export function ProfileEditor({ visible, initial, onSave, onCancel }: Props) {
  const def = defaultProfile();

  const [form, setForm] = useState<Record<string, string>>(() => ({
    name:          initial?.name         ?? '',
    targetPackage: initial?.targetPackage ?? '',
    ipv4:          initial?.ipv4          ?? def.ipv4,
    ipv6:          initial?.ipv6          ?? def.ipv6,
    manufacturer:  initial?.manufacturer  ?? def.manufacturer,
    model:         initial?.model         ?? def.model,
    androidId:     initial?.androidId     ?? def.androidId,
    fingerprint:   initial?.fingerprint   ?? def.fingerprint,
    brand:         initial?.brand         ?? def.brand,
    device:        initial?.device        ?? def.device,
    product:       initial?.product       ?? def.product,
    buildId:       initial?.buildId       ?? def.buildId,
    sdkInt:        initial?.sdkInt        ?? def.sdkInt,
  }));

  const [errors, setErrors] = useState<Record<string, string>>({});

  const set = useCallback((key: string) => (val: string) => {
    setForm(prev => ({ ...prev, [key]: val }));
    setErrors(prev => { const e = { ...prev }; delete e[key]; return e; });
  }, []);

  const randomizeAll = () => {
    const r = generateFullRandom();
    setForm(prev => ({
      ...prev,
      ipv4: r.ipv4, ipv6: r.ipv6,
      manufacturer: r.manufacturer, model: r.model,
      androidId: r.androidId, fingerprint: r.fingerprint,
      brand: r.brand, device: r.device, product: r.product,
      buildId: r.buildId, sdkInt: r.sdkInt,
    }));
    setErrors({});
  };

  const handleSave = () => {
    const errs = validate(form);
    if (Object.keys(errs).length > 0) { setErrors(errs); return; }
    const profile: SpoofProfile = {
      id:           initial?.id      ?? `${Date.now()}-${Math.random().toString(36).slice(2)}`,
      createdAt:    initial?.createdAt ?? Date.now(),
      enabled:      initial?.enabled  ?? true,
      name:          form.name.trim(),
      targetPackage: form.targetPackage.trim(),
      ipv4:          form.ipv4,
      ipv6:          form.ipv6,
      manufacturer:  form.manufacturer,
      model:         form.model,
      androidId:     form.androidId,
      fingerprint:   form.fingerprint,
      brand:         form.brand,
      device:        form.device,
      product:       form.product,
      buildId:       form.buildId,
      sdkInt:        form.sdkInt,
    };
    onSave(profile);
  };

  return (
    <Modal visible={visible} animationType="slide" presentationStyle="pageSheet" onRequestClose={onCancel}>
      <KeyboardAvoidingView behavior={Platform.OS === 'ios' ? 'padding' : 'height'} style={{ flex: 1 }}>
        <View style={styles.container}>
          {/* Top bar */}
          <View style={styles.topBar}>
            <Pressable onPress={onCancel} hitSlop={10}>
              <MaterialIcons name="close" size={22} color={Colors.textSecondary} />
            </Pressable>
            <Text style={styles.title}>{initial ? 'Edit Profile' : 'New Profile'}</Text>
            <Pressable onPress={randomizeAll} style={styles.rndAll} hitSlop={8}>
              <MaterialIcons name="auto-fix-high" size={16} color={Colors.accent} />
              <Text style={styles.rndAllText}>ALL RND</Text>
            </Pressable>
          </View>

          <ScrollView style={{ flex: 1 }} contentContainerStyle={styles.scroll} showsVerticalScrollIndicator={false}>

            {/* === GENERAL === */}
            <SectionHeader label="GENERAL" />
            <FieldInput label="Profile Name" value={form.name} onChange={set('name')} placeholder="My Spoof Profile" error={errors.name} />
            <FieldInput label="Target Package" value={form.targetPackage} onChange={set('targetPackage')} placeholder="com.example.app (leave blank for all)" monospace />

            {/* === NETWORK === */}
            <SectionHeader label="NETWORK" />
            <FieldInput label="IPv4 Address" value={form.ipv4} onChange={set('ipv4')} monospace onRandomize={() => set('ipv4')(randomIPv4())} error={errors.ipv4} />
            <FieldInput label="IPv6 Address" value={form.ipv6} onChange={set('ipv6')} monospace onRandomize={() => set('ipv6')(randomIPv6())} />

            {/* === DEVICE === */}
            <SectionHeader label="DEVICE IDENTITY" />
            <FieldInput label="Manufacturer" value={form.manufacturer} onChange={set('manufacturer')} />
            <FieldInput label="Model" value={form.model} onChange={set('model')} monospace />
            <FieldInput label="Brand" value={form.brand} onChange={set('brand')} monospace />
            <FieldInput label="Device" value={form.device} onChange={set('device')} monospace />
            <FieldInput label="Product" value={form.product} onChange={set('product')} monospace />

            {/* === BUILD === */}
            <SectionHeader label="BUILD INFO" />
            <FieldInput label="Android ID" value={form.androidId} onChange={set('androidId')} monospace onRandomize={() => set('androidId')(randomAndroidId())} error={errors.androidId} placeholder="16 hex chars" />
            <FieldInput label="Build ID" value={form.buildId} onChange={set('buildId')} monospace onRandomize={() => set('buildId')(randomBuildId())} />
            <FieldInput label="SDK Int" value={form.sdkInt} onChange={set('sdkInt')} monospace error={errors.sdkInt} placeholder="33" />
            <FieldInput
              label="Fingerprint"
              value={form.fingerprint}
              onChange={set('fingerprint')}
              monospace
              onRandomize={() => set('fingerprint')(randomFingerprint(form.manufacturer, form.model, form.buildId))}
              multiline
            />

          </ScrollView>

          {/* Bottom actions */}
          <View style={styles.footer}>
            <GlowButton label="Cancel" onPress={onCancel} variant="ghost" style={{ flex: 1 }} />
            <GlowButton label="Save Profile" onPress={handleSave} variant="primary" style={{ flex: 2 }} />
          </View>
        </View>
      </KeyboardAvoidingView>
    </Modal>
  );
}

function SectionHeader({ label }: { label: string }) {
  return (
    <View style={secStyles.wrap}>
      <Text style={secStyles.text}>{label}</Text>
      <View style={secStyles.line} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: Colors.bg },
  topBar:    { flexDirection: 'row', alignItems: 'center', paddingHorizontal: Spacing.md, paddingVertical: Spacing.md, borderBottomWidth: 1, borderColor: Colors.border },
  title:     { flex: 1, textAlign: 'center', fontSize: FontSize.lg, fontWeight: FontWeight.bold, color: Colors.textPrimary },
  rndAll:    { flexDirection: 'row', alignItems: 'center', gap: 4 },
  rndAllText:{ fontSize: FontSize.xs, fontWeight: FontWeight.bold, color: Colors.accent },
  scroll:    { padding: Spacing.md, paddingBottom: Spacing.xxl },
  footer:    { flexDirection: 'row', gap: Spacing.sm, padding: Spacing.md, borderTopWidth: 1, borderColor: Colors.border, backgroundColor: Colors.bg },
});

const secStyles = StyleSheet.create({
  wrap: { flexDirection: 'row', alignItems: 'center', gap: Spacing.sm, marginTop: Spacing.md, marginBottom: Spacing.sm },
  text: { fontSize: FontSize.xs, fontWeight: FontWeight.bold, color: Colors.textLabel, letterSpacing: 1.5 },
  line: { flex: 1, height: 1, backgroundColor: Colors.border },
});
